package bob.bell

import bob.v2.*

import static bob.PrettyPrint.prettyPrint

class FactoryBell extends FactoryBuilderSupport {

    public FactoryBell(boolean init = true) {
        super(init)
    }
    def registerNodes() {
        registerBeanFactory('house', House)
        registerFactory('roof', new RoofFactory())
        registerFactory('level', new LevelFactory())
        registerFactory('material', new MaterialFactory())
        registerFactory('room', new RoomFactory())
        // tag::bell1[]
    registerFactory('bell', new BellFactory())
        // end::bell1[]
    }
    // tag::bell2[]
    static main(def args) {
        House house = this.newInstance(true).house(number: 1) {
            bell {
                println "The door rings"
            }
            material('Bricks')
            roof('tiles', color: 'Red')
            level('1nd floor') {
                room(name: 'Bedroom')
                room('Childrens room')
            }
        }
        prettyPrint house
        house.ring()
    }
    // end::bell2[]
}

