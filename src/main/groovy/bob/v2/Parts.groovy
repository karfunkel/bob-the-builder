package bob.v2

import groovy.transform.Canonical

// tag::house[]
@Canonical
class House {
    int number
    Material material
    Roof roof
    List<Level> level = []
    Closure bell

    void ring() {
        bell?.call()
    }
}
// end::house[]

// tag::level[]
@Canonical
class Level {
    String floor
    List<Room> rooms = []
}
// end::level[]

// tag::room[]
@Canonical(excludes = ['doors'])
class Room {
    String name
    List<Room> rooms = []
    Set<Door> doors = []
    double temperature
}
// end::room[]

// tag::door[]
@Canonical
class Door {
    Room one
    Room two

    String prettyPrint(String prefix) {
        println "${prefix}<Door>"
        println "${prefix}room: $one.name"
        println "${prefix}room: $two.name"
    }
}
// end::door[]

// tag::roof[]
@Canonical
class Roof {
    static String MATERIAL_TILES = 'Tiles'
    static String MATERIAL_REED = 'Reed'
    static String MATERIAL_SHINGLES = 'Shingles'

    String material
    String color
}
// end::roof[]

// tag::material[]
enum Material {
    Bricks, Wood, Concrete
}
// end::material[]
