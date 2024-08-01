package neu.vansven.thirdparty_interface.domain;

import lombok.Data;

@Data
public class Person {


    String name;
    int age;
    String gender;

    public Person() {
    }

    public Person(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
}
