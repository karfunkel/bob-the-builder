package bob.temperature

import bob.v1.*

import static bob.PrettyPrint.prettyPrint


class FactoryTemperature extends FactoryBuilderSupport {
    // tag::temperature1[]
    public FactoryTemperature(boolean init = true) {
        super(init)
        setVariable('roomTemperature', [:])
    }

    def registerNodes() {
        registerBeanFactory('house', House)
        registerFactory('roof', new RoofFactory())
        registerFactory('level', new LevelFactory())
        registerFactory('material', new MaterialFactory())
        registerFactory('room', new RoomFactory())

        addAttributeDelegate { FactoryBuilderSupport builder, Object node, Map attributes ->
            def temperature = attributes.remove 'temperature'
            if (temperature instanceof Number)
                builder.context.temperature = temperature
            else {
                temperature = builder.parentContext.temperature
                if (temperature)
                    builder.context.temperature = temperature
            }
        }
    }
    // end::temperature1[]
    // tag::temperature2[]
    static main(def args) {
        def builder = this.newInstance(true)
        prettyPrint builder.house(number: 1, temperature: 60) {
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
            level('Basement', temperature: 55) {
                room('Laundry')
                room('Cellar', temperature: 50) {
                    room('Food storage', temperature: 40)
                    room('Store room')
                }
            }
        }

        println "Room Temperatures: $builder.roomTemperature"
    }
    // end::temperature2[]
}

class FactoryTemperature2 extends FactoryBuilderSupport {


    public FactoryTemperature2(boolean init = true) {
        super(init)
        setVariable('roomTemperature', [:])
    }

    def registerNodes() {
        registerBeanFactory('house', House)
        registerFactory('roof', new RoofFactory())
        registerFactory('level', new LevelFactory())
        registerFactory('material', new MaterialFactory())
        registerFactory('room', new RoomFactory())
        // tag::temperature3[]
registerFactory('heating', new TemperatureFactory())
registerFactory('cooler', new TemperatureFactory())
        // end::temperature3[]

        addAttributeDelegate { FactoryBuilderSupport builder, Object node, Map attributes ->
            def temperature = attributes.remove 'temperature'
            if (temperature instanceof Number)
                builder.context.temperature = temperature
            else {
                temperature = builder.parentContext.temperature
                if (temperature)
                    builder.context.temperature = temperature
            }
        }
    }
    // tag::temperature4[]
    static main(def args) {
        def builder = this.newInstance(true)
        prettyPrint builder.house(number: 1, temperature: 60) {
            material('Bricks')
            roof('tiles', color: 'Red')
            level('1nd floor') {
                room(name: 'Bedroom') {
                    cooler(-5)
                }
                room('Childrens room') {
                    heating(5)
                }
            }
            level('Basement', temperature: 55) {
                room('Laundry')
                room('Cellar', temperature: 50) {
                    room('Food storage', temperature: 40)
                    room('Store room')
                }
            }
        }
        println "Room Temperatures: $builder.roomTemperature"
    }
    // end::temperature4[]
}