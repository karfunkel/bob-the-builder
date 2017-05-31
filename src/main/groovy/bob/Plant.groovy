package bob

import bob.v1.*

import static bob.PrettyPrint.*

// tag::plant1[]
class Plant1 extends BuilderSupport {
    @Override protected Object createNode(Object name, Map attributes = [:]) {
        return createNode(name, attributes, null)
    }

    @Override protected Object createNode(Object name, Map attributes = [:], Object value) {
        switch (name) {
            case 'house': return new House(attributes)
            default: return null
        }
    }

    @Override protected void setParent(Object parent, Object child) {}

    static main(def args) {
        House house = this.newInstance().house(number: 1) {}
        prettyPrint(house)
    }
}
// end::plant1[]

class Plant2 extends BuilderSupport {

    @Override
    protected Object createNode(Object name, Map attributes = [:]) {
        return createNode(name, attributes, null)
    }
    // tag::plant2[]
    @Override protected Object createNode(Object name, Map attributes = [:], Object value){
        switch (name) {
            case 'house': return new House(attributes)
            case 'roof': return new Roof(attributes + [material: value])
            default: return null
        }
    }

    @Override protected void setParent(Object parent, Object child) {
        switch (child) {
            case Roof: parent.roof = child; break
        }
    }

    static main(def args) {
        House house = this.newInstance().house(number: 1) {
            roof('tiles', color: 'Red')
        }
        prettyPrint(house)
    }
    // end::plant2[]
}

class Plant3 extends BuilderSupport {

    @Override
    protected Object createNode(Object name, Map attributes = [:]) {
        return createNode(name, attributes, null)
    }
    // tag::plant3[]
    @Override protected Object createNode(Object name, Map attributes = [:], Object value){
        switch (name) {
            case 'house': return new House(attributes)
            case 'roof': return new Roof(attributes + [material: value])
            case 'level': return new Level(floor: value)
            default: return null
        }
    }

    @Override protected void setParent(Object parent, Object child) {
        switch (child) {
            case Roof: parent.roof = child; break
            case Level: parent.level << child; break
        }
    }
    // end::plant3[]

    // tag::main3[]
    static main(def args) {
        House house = this.newInstance().house(number: 1) {
            roof('tiles', color: 'Red')
            level('1nd floor') {}
            level('Ground floor') {}
            level('Basement') {}
        }
        prettyPrint(house)
    }
    // end::main3[]
}

class Plant4 extends BuilderSupport {

    @Override
    protected Object createNode(Object name, Map attributes = [:]) {
        return createNode(name, attributes, null)
    }

    // tag::plant4[]
    @Override protected Object createNode(Object name, Map attributes = [:], Object value) {
        switch (name) {
            case 'house': return new House(attributes)
            case 'roof': return new Roof(attributes + [material: value])
            case 'level': return new Level(floor: value)
            case 'material':
                return value instanceof Material ? value : Material.valueOf(value.toString())
            default: return null
        }
    }

    @Override protected void setParent(Object parent, Object child) {
        switch (child) {
            case Roof: parent.roof = child; break
            case Level: parent.level << child; break
            case Material: parent.material = child; break
        }
    }
    // end::plant4[]

    // tag::main4[]
    static main(def args) {
        House house = this.newInstance().house(number: 1) {
            material('Bricks')
            roof('tiles', color: 'Red')
            level('1nd floor') {}
            level('Ground floor') {}
            level('Basement') {}
        }
        prettyPrint(house)
    }
    // end::main4[]
}

class Plant5 extends BuilderSupport {

    @Override
    protected Object createNode(Object name, Map attributes = [:]) {
        return createNode(name, attributes, null)
    }

    // tag::plant5[]
    @Override protected Object createNode(Object name, Map attributes = [:], Object value) {
        switch (name) {
            case 'house': return new House(attributes)
            case 'roof': return new Roof(attributes + [material: value])
            case 'level': return new Level(floor: value)
            case 'material':
                return value instanceof Material ? value : Material.valueOf(value.toString())
            case 'room':
                String roomName = value ?: attributes.remove('name')
                return new Room(attributes + [name: roomName])
            default: return null
        }
    }

    @Override protected void setParent(Object parent, Object child) {
        switch (child) {
            case Roof: parent.roof = child; break
            case Level: parent.level << child; break
            case Material: parent.material = child; break
            case Room: parent.rooms << child; break
        }
    }
    // end::plant5[]

    // tag::main5[]
    static main(def args) {
        House house = this.newInstance().house(number: 1) {
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
        prettyPrint(house)
    }
    // end::main5[]
}

