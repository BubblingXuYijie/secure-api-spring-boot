package icu.xuyijie.entity;

/**
 * @author 徐一杰
 * @date 2024/7/29 10:00
 * @description
 */
public class People {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                '}';
    }
}
