package pl.gienius.pisarz;

import java.io.Serializable;
import java.util.Objects;

public class Writer implements Serializable {
    private Long id;
    private String name;



    public Writer(String name) {
        //this.id = null;
        this.name = name;
    }

    public Writer(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Writer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Writer writer = (Writer) o;
        return Objects.equals(getName(), writer.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
