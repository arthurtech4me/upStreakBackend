package com.upstreak.habits.model;

import com.upstreak.habits.DTOs.AchievementDTO;
import com.upstreak.habits.model.enums.AchievementCode;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tb_achievements")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AchievementCode code;

    private String name;
    private String description;

    @OneToMany(mappedBy = "achievement")
    private List<UserAchievement> userAchievements;

    public static Achievement from(AchievementDTO achievementDTO) {
        return new Achievement(achievementDTO.id(), achievementDTO.code(), achievementDTO.name(), achievementDTO.description());
    }

    public Achievement() {
    }

    public Achievement(Long id, AchievementCode code, String name, String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AchievementCode getCode() {
        return code;
    }

    public void setCode(AchievementCode code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
