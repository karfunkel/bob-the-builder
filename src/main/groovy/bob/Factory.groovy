package bob

import bob.v1.*

import static bob.PrettyPrint.prettyPrint


// tag::factory1[]
class Factory extends FactoryBuilderSupport {
    public Factory(boolean init = true) {
        super(init)
    }

    def registerNodes() {
        registerBeanFactory('house', House)
    }

    static main(def args) {
        prettyPrint this.newInstance(true).house(number: 1)
    }
}
// end::factory1[]

class Factory2 extends FactoryBuilderSupport {

    public Factory2(boolean init = true) {
        super(init)
    }
    // tag::factory2[]
    def registerNodes() {
        registerBeanFactory('house', House)
        registerFactory('roof', new RoofFactory())
    }

    static main(def args) {
        prettyPrint this.newInstance(true).house(number: 1) {
            roof('tiles', color: 'Red')
        }
    }
    // end::factory2[]
}

// tag::factory2f[]
class RoofFactory extends AbstractFactory {
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
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
// end::factory2f[]

class Factory3 extends FactoryBuilderSupport {

    public Factory3(boolean init = true) {
        super(init)
    }
    // tag::factory3[]
    def registerNodes() {
        registerBeanFactory('house', House)
        registerFactory('roof', new RoofFactory())
        registerFactory('level', new LevelFactory())
    }

    static main(def args) {
        prettyPrint this.newInstance(true).house(number: 1) {
            roof('tiles', color: 'Red')
            level('1nd floor') {}
            level('Ground floor') {}
            level('Basement') {}
        }
    }
    // end::factory3[]
}

// tag::factory3f[]
class LevelFactory extends AbstractFactory {
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        return new Level(floor: attributes.remove('floor') ?: value)
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof House)
            parent.level << child
    }
}
// end::factory3f[]

class Factory4 extends FactoryBuilderSupport {

    public Factory4(boolean init = true) {
        super(init)
    }
    // tag::factory4[]
    def registerNodes() {
        registerBeanFactory('house', House)
        registerFactory('roof', new RoofFactory())
        registerFactory('level', new LevelFactory())
        registerFactory('material', new MaterialFactory())
    }

    static main(def args) {
        prettyPrint this.newInstance(true).house(number: 1) {
            material('Bricks')
            roof('tiles', color: 'Red')
            level('1nd floor') {}
            level('Ground floor') {}
            level('Basement') {}
        }
    }
    // end::factory4[]
}

// tag::factory4f[]
class MaterialFactory extends AbstractFactory {
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        return value instanceof Material ? value : Material.valueOf(value)
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof House)
            parent.material = child
    }
}
// end::factory4f[]

class Factory5 extends FactoryBuilderSupport {

    public Factory5(boolean init = true) {
        super(init)
    }
    // tag::factory51[]
    def registerNodes() {
        registerBeanFactory('house', House)
        registerFactory('roof', new RoofFactory())
        registerFactory('level', new LevelFactory())
        registerFactory('material', new MaterialFactory())
        registerFactory('room', new RoomFactory())
    }
    // end::factory51[]
    // tag::factory52[]
    static main(def args) {
        prettyPrint this.newInstance(true).house(number: 1) {
            material('Bricks')
            roof('tiles', color: 'Red')
            level('1nd floor') {
                room(name: 'Bedroom')
                room('Childrens room')
            }
            level('Ground floor') {
                room('Living room')
                room('Kitchen')
            }
            level('Basement') {
                room('Laundry')
                room('Cellar') {
                    room('Food storage')
                    room('Store room')
                }
            }
        }
    }
    // end::factory52[]
}

// tag::factory5f[]
class RoomFactory extends AbstractFactory {
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        return new Room((attributes.remove('name') ?: value)?.toString())
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof Level)
            parent.rooms << child
//        else if (parent instanceof Room)
//            parent.rooms << child
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof Room)
            parent.rooms << child
    }
}
// end::factory5f[]