package PlaceDB;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "food")
public class Food extends Places{
}