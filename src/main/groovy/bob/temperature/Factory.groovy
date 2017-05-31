package bob.temperature

import bob.v1.House
import bob.v1.Level
import bob.v1.Roof
import bob.v1.Room

class RoofFactory extends AbstractFactory {
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        return new Roof(material: attributes.remove('material') ?: value)
    }

    @Override
    boolean isLeaf() {
        true
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof House)
            parent.roof = child
    }
}

class LevelFactory extends AbstractFactory {
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        return new Level(floor: attributes.remove('floor') ?: value)
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof House)
            parent.level << child
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

// tag::roomFactory[]
class RoomFactory extends AbstractFactory {
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        return new Room((attributes.remove('name') ?: value)?.toString())
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof Level) parent.rooms << child
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof Room) parent.rooms << child
    }

    @Override
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        Number temperature = builder.context.temperature
        if(node instanceof Room)
            builder.roomTemperature[node.name] = temperature
        return true
    }
}
// end::roomFactory[]

// tag::temperatureFactory[]
class TemperatureFactory extends AbstractFactory {
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        return [modifier: value]
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        Number temperature = builder.parentContext.temperature + child.modifier
        builder.roomTemperature[parent.name] = temperature
    }
}
// end::temperatureFactory[]
