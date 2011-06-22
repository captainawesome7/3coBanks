package me.ic3d.ecobanks;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotEmpty;

/**
 *
 * @author IC3D (its sammy's tutorial database thingy, idk)
 */
@Entity()
@Table(name = "Banks")
public class EBP {
    @Id
    @GeneratedValue
    private int id; // Database Id
    @NotEmpty
    private String PlayerName; //Minecraft account name
    private Integer Savings;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPlayerName() {
		return PlayerName;
	}
	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}
	public Integer getSavings() {
		return Savings;
	}
	public void setSavings(Integer savings) {
		Savings = savings;
	}
}