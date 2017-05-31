package bob.v1

import groovy.transform.Canonical

// tag::house[]
@Canonical
class House {
    int number
    Material material
    Roof roof
    List<Level> level = []
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
@Canonical
class Room {
    String name
    List<Room> rooms = []
}
// end::room[]

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
