package newspaper.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Customers")
public class Customer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private int Id;

    @Column(name="Name")
    private String Name;

    Customer()
    {
        // default constructors
    }

    Customer(int Id, String Name)
    {
        this.Id = Id;
        this.Name = Name;
    }
}
