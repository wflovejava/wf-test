package com.wf;

import java.io.Serializable;

/**
 * @Author: wf
 * @Date: 2020-05-08 18:31
 * @describe
 */
public class Person implements Serializable {

    /**
     * name
     */
    String name;
    /**
     * age
     */
    int age;
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    @Override
    public String toString()
    {
        return super.toString() + "\nname: " + this.getName() + "\tage: " + this.getAge();
    }
}
