package quokka.todayflowers.domain.repository.custom;

import quokka.todayflowers.domain.entity.Flower;

import java.util.Optional;

public interface FlowerRepositoryCustom {
    Optional<Flower> findFlowerBy(int month, int day, String userId);
}
