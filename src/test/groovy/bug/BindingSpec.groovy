package bug

import grails.web.databinding.DataBindingUtils
import spock.lang.Specification

class BindingSpec extends Specification {

    void 'when binding with an interface, the databinder in Grails fails'() {
        given:
        Map input = [a: [data: 'abc']]
        ClassBWithInterface b = new ClassBWithInterface()

        when:
        DataBindingUtils.bindObjectToInstance(b, input)

        then: 'this line fails, because SimpleDataBinder#624 tries to instantiate InterfaceA instead of ClassA'
        b.a.data == 'abc'
    }

    void 'when binding with a class, the databinder in Grails works'() {
        given:
        Map input = [a: [data: 'abc']]
        ClassBNoInterface b = new ClassBNoInterface()

        when:
        DataBindingUtils.bindObjectToInstance(b, input)

        then:
        b.a.data == 'abc'
    }

    void 'showing problem in SimpleDataBinder line 623'() {
        given:
        ClassBWithInterface b = new ClassBWithInterface()

        when:
        MetaBeanProperty mbp = b.metaClass.getMetaProperty('a') as MetaBeanProperty

        then:
        mbp.getter.returnType == InterfaceA // Takes from the interface and tries to instantiate it.
        mbp.field.type == ClassA
    }
}
