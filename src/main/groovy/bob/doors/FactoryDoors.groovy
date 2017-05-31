package bob.doors

import bob.v2.House

import static bob.PrettyPrint.prettyPrint

class FactoryDoors extends FactoryBuilderSupport {
    public FactoryDoors(boolean init = true) {
        super(init)
    }

    def registerNodes() {
        registerBeanFactory('house', House)
        registerFactory('roof', new RoofFactory())
        registerFactory('level', new LevelFactory())
        registerFactory('material', new MaterialFactory())
        registerFactory('room', new RoomFactory())
        // tag::door1[]
registerFactory('door', new DoorFactory())
        // end::door1[]
    }

    // tag::door2[]
    static main(def args) {
        prettyPrint this.newInstance(true).house(number: 1) {
            level('1nd floor') {
                room(name: 'Bedroom')
                room('Childrens room') {
                    door('Bedroom')
                }
            }
            level('Ground floor') {
                room('Living room') {
                    door('Kitchen')
                }
                room('Kitchen')
            }
            level('Basement') {
                room('Laundry') {
                    door('Food storage')
                }
                room('Cellar') {
                    room('Food storage') {
                        door('Laundry')
                    }
                    room('Store room') {
                        door('Laundry')
                    }
    }   }   }   }
    // end::door2[]
}

