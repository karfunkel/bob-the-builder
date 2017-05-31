package bob.doors

import bob.v2.*

class RoofFactory extends AbstractFactory {
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        return new Roof(material: attributes.remove('material') ?: value)
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof House)
            parent.roof = child
    }
}


class MaterialFactory extends AbstractFactory {
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        return value
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof House)
            parent.material = child
    }
}

// tag::RoomFactory[]
class RoomFactory extends AbstractFactory {
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        Room room = new Room((attributes.remove('name') ?: value)?.toString())
        getLevelContext(builder).roomMap."$room.name" = room
        return room
    }
    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof Level)
            parent.rooms << child
    }
    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof Room)
            parent.rooms << child
    }

    Map getLevelContext(FactoryBuilderSupport builder) {
        Map levelContext = builder.parentContext
        while (levelContext."$FactoryBuilderSupport.CURRENT_NAME" != 'level')
            levelContext = levelContext."$FactoryBuilderSupport.PARENT_CONTEXT"
        return levelContext
    }
}
// end::RoomFactory[]

// tag::LevelFactory[]
class LevelFactory extends AbstractFactory {
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        builder.context.roomMap = [:]
        builder.context.freeDoors = [:]
        return new Level(floor: attributes.remove('floor') ?: value)
    }
    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof House) parent.level << child
    }
    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        builder.context.freeDoors.each { String name, Room room ->
            def other = builder.context.roomMap."$name"
            if (other) {
                Door door = new Door(room, other)
                other.doors << door
                room.doors << door
            }
        }
}   }
// end::LevelFactory[]

// tag::DoorFactory[]
class DoorFactory extends AbstractFactory {
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        return [from: value]
    }
    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        Map levelContext = builder.parentFactory.getLevelContext(builder)
        Room other = levelContext.roomMap."$child.from"

        if (other) {
            Door door = new Door(other, parent)
            other.doors << door
            parent.doors << door
        } else {
            levelContext.freeDoors."$child.from" = parent
        }
    }
}
// end::DoorFactory[]