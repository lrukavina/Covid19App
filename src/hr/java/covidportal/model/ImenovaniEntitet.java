package hr.java.covidportal.model;

import java.util.Objects;

/**
 * Služi za postavljanje naziva entitetu koji nasljeđuje ovu klasu.
 */
public abstract class ImenovaniEntitet {
    private Long id;
    private String naziv;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImenovaniEntitet)) return false;
        ImenovaniEntitet that = (ImenovaniEntitet) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getNaziv(), that.getNaziv());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNaziv());
    }

    @Override
    public String toString() {
        return "ImenovaniEntitet{" +
                "id=" + id +
                ", naziv='" + naziv + '\'' +
                '}';
    }

    /**
     * Služi za postavljanje naziva entitetu koji nasljeđuje ovu klasu.
     * @param naziv Naziv entiteta koji nasljeđuje ovu klasu.
     */
    public ImenovaniEntitet(Long id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
