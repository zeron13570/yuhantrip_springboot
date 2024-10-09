package PlaceDB;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "cafe")
public class Cafe extends Places{
}