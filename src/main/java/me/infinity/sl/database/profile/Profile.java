package me.infinity.sl.database.profile;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.Getter;
import me.infinity.sl.SimpleLifeSteal;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Data
@DatabaseTable(tableName = "LIFESTEAL_SMP")
public class Profile {

  @Getter private static final Dao<Profile, UUID> dao = SimpleLifeSteal.getInstance().getHikariDatabase().getProfileDao();
  @Getter private static final Map<UUID, Profile> cache = new HashMap<>();

  @DatabaseField(columnName = "UUID", id = true, dataType = DataType.UUID)
  private UUID uniqueID;

  @DatabaseField(columnName = "UUID", dataType = DataType.STRING)
  private String username;

  @DatabaseField(columnName = "ELIMINATED", dataType = DataType.BOOLEAN)
  private boolean isEliminated;

  public Profile() {}

  public Profile(UUID uniqueID) {
    this.uniqueID = uniqueID;
  }

  public Profile get(String username) throws SQLException {
    Optional<Profile> profile = Optional.ofNullable(dao.queryForId(uniqueID));
    return profile.orElseGet(() -> {
      try {
        return this.save(username);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public Profile save(String username) throws SQLException {
    Optional<Profile> profile = Optional.ofNullable(dao.queryForId(uniqueID));
    if (profile.isPresent()) dao.update(profile.get());
    Profile prf = new Profile(uniqueID);
    prf.setEliminated(false);
    prf.setUsername(username);
    dao.create(prf);
    return prf;
  }

}
