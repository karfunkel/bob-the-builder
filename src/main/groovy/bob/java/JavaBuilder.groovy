package bob.java

import static bob.PrettyPrint.prettyPrint

class JavaBuilder {
  // tag::main[]
  static main(def args) {
      prettyPrint House.builder()
          .number(1)
          .material(Material.Bricks)
          .roof(Roof.builder().material(Roof.MATERIAL_TILES).color('Red').build())
          .level([
          Level.builder().floor('1nd floor').rooms([
                  Room.builder().name('Bedroom').build(),
                  Room.builder().name('Childrens room').build()
          ]).build(),
          Level.builder().floor('Ground floor').rooms([
                  Room.builder().name('Living room').build(),
                  Room.builder().name('Kitchen').build()
          ]).build(),
          Level.builder().floor('Basement').rooms([
                  Room.builder().name('Laundry').build(),
                  Room.builder().name('Cellar').rooms([
                          Room.builder().name('Food storage').build(),
                          Room.builder().name('Store room').build()
                  ]).build()
          ]).build()
      ]).build()
  }
  // end::main[]
}
