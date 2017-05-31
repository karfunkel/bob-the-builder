package bob

import bob.v1.*

import static PrettyPrint.*

// tag::handcraft1[]
class Handcraft1 {
    House house(Closure blueprint) {
        House house = new House()
        Closure plan = blueprint.clone()
        plan.delegate = this
        plan.resolveStrategy = Closure.DELEGATE_ONLY // OR DELEGATE_FIRST
        plan()
        return house
    }

    static main(def args) {
        House house = this.newInstance().house {}
        prettyPrint(house)
    }
}
// end::handcraft1[]

// tag::handcraft2[]

import static bob.v1.Material.Bricks

class Handcraft2 {
    House house(Map attributes = [:], Closure blueprint = {}) {
        House house = new House(attributes)
        Closure plan = blueprint.clone()
        plan.delegate = this
        plan.resolveStrategy = Closure.DELEGATE_ONLY
        plan()
        return house
    }

    static main(def args) {
        House house = this.newInstance().house(number: 1, material: Bricks)
        prettyPrint(house)
    }
}
// end::handcraft2[]

class Handcraft3 {
// tag::handcraft31[]
    House house

    House house(Map attributes = [:], Closure blueprint = {}) {
        house = new House(attributes)
// end::handcraft31[]
        Closure plan = blueprint.clone()
        plan.delegate = this
        plan.resolveStrategy = Closure.DELEGATE_ONLY
        plan()
        return house
    }

// tag::handcraft3[]
    void roof(Map attributes = [:], String material) {
        Roof roof = new Roof(attributes)
        roof.material = material
        house.roof = roof
    }

    static main(def args) {
        House house = this.newInstance().house(number: 1, material: Bricks) {
            roof('tiles', color: 'Red')
        }
        prettyPrint(house)
    }
// end::handcraft3[]
}


class Handcraft4R {
// tag::handcraft4r[]
    House house

    House house(Map attributes = [:], Closure blueprint = {}) {
        house = new House(attributes)
        runClosure(blueprint)
        return house
    }

    private runClosure(Closure cls) {
        if (cls) {
            Closure clone = cls.clone()
            clone.delegate = this
            clone.resolveStrategy = Closure.DELEGATE_ONLY
            clone()
        }
    }
    // ...
    // end::handcraft4r[]
}

class Handcraft4 {
    House house

    House house(Map attributes = [:], Closure blueprint = {}) {
        house = new House(attributes)
        runClosure(blueprint, house)
        return house
    }

    // tag::handcraft41[]
    List stack = []
    def parent
    def current

    private runClosure(Closure cls, def instance) {
        parent = stack ? stack?.last() : null
        stack.push(instance)
        current = instance

        if (cls) {
            Closure clone = cls.clone()
            clone.delegate = this
            clone.resolveStrategy = Closure.DELEGATE_ONLY
            clone()
        }

        current = stack ? stack.last() : null
        stack.pop()
        parent = stack ? stack.last() : null
    }
    // end::handcraft41[]

    void roof(Map attributes = [:], String material) {
        Roof roof = new Roof(attributes)
        roof.material = material
        house.roof = roof
    }

    // tag::handcraft42[]
    void level(String floor, Closure blueprint = {}) {
        Level level = new Level(floor: floor)
        runClosure(blueprint, level)
        parent.level << level
    }
    // end::handcraft42[]

    // tag::handcraft4main[]
    static main(def args) {
        House house = this.newInstance().house(number: 1, material: Bricks) {
            roof('tiles', color: 'Red')
            level('1nd floor')
            level('Ground floor')
            level('Basement')
        }
        prettyPrint(house)
    }
    // end::handcraft4main[]
}

class Handcraft5 {
    House house

    List stack = []
    def parent
    def current

    House house(Map attributes = [:], Closure blueprint = {}) {
        house = new House(attributes)
        runClosure(blueprint, house)
        return house
    }

    private runClosure(Closure cls, def instance) {
        parent = stack ? stack.last() : null
        stack.push(instance)
        current = instance

        if (cls) {
            Closure clone = cls.clone()
            clone.delegate = this
            clone.resolveStrategy = Closure.DELEGATE_ONLY
            clone()
        }

        current = stack ? stack.last() : null
        stack.pop()
        parent = stack ? stack.last() : null
    }

    void roof(Map attributes = [:], String material) {
        Roof roof = new Roof(attributes)
        roof.material = material
        house.roof = roof
    }

    void level(String floor, Closure blueprint = {}) {
        Level level = new Level(floor: floor)
        runClosure(blueprint, level)
        parent.level << level
    }
    // tag::handcraft5[]
    void material(def material) {
        if (material instanceof String)
            material = Material.valueOf(material)
        current.material = material
    }

    static main(def args) {
        House house = this.newInstance().house(number: 1) {
            material('Bricks')
            roof('tiles', color: 'Red')
            level('1nd floor')
            level('Ground floor')
            level('Basement')
        }
        prettyPrint(house)
    }
    // end::handcraft5[]
}

class Handcraft6 {
    House house

    List stack = []
    def parent
    def current

    House house(Map attributes = [:], Closure blueprint = {}) {
        house = new House(attributes)
        runClosure(blueprint, house)
        return house
    }

    private runClosure(Closure cls, def instance) {
        parent = stack ? stack.last() : null
        stack.push(instance)
        current = instance

        if (cls) {
            Closure clone = cls.clone()
            clone.delegate = this
            clone.resolveStrategy = Closure.DELEGATE_ONLY
            clone()
        }

        current = stack ? stack.last() : null
        stack.pop()
        parent = stack ? stack.last() : null
    }

    void roof(Map attributes = [:], String material) {
        Roof roof = new Roof(attributes)
        roof.material = material
        house.roof = roof
    }

    void level(String floor, Closure blueprint = {}) {
        Level level = new Level(floor: floor)
        runClosure(blueprint, level)
        parent.level << level
    }

    void material(def material) {
        if (material instanceof String)
            material = Material.valueOf(material)
        current.material = material
    }

    // tag::handcraft6[]
    void room(String name, Closure blueprint = null) {
        room([:], name, blueprint)
    }

    void room(Map attributes, String name = null, Closure blueprint = null) {
        Room room = new Room(attributes)
        if (name != null) room.name = name
        runClosure(blueprint, room)
        parent.rooms << room
    }
    // end::handcraft6[]

    // tag::main6[]
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
    // end::main6[]
}

