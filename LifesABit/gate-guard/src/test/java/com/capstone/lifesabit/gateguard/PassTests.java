package com.capstone.lifesabit.gateguard;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.capstone.lifesabit.gateguard.passes.Pass;

public class PassTests {
  @Test
  void should_retTrue_whenPassExpired() {
    // given
    Pass pass = new Pass("John", "Jacob", "jingleheimer@smith.com", UUID.randomUUID(), System.currentTimeMillis() - 100);
    
    // when
    boolean isExpired = pass.isExpired();

    // then
    assertTrue(isExpired);
  }

  @Test
  void should_retTrue_whenPassNotExpired() {
    // given
    Pass pass = new Pass("John", "Jacob", "jingleheimer@smith.com", UUID.randomUUID(), System.currentTimeMillis() + 100);
    
    // when
    boolean isExpired = pass.isExpired();

    // then
    assertFalse(isExpired);
  }

  @Test
  void should_retTrue_whenPassOutOfUses() {
    // given
    Pass pass = new Pass(UUID.randomUUID(), "John", "Jacob", "jingleheimer@smith.com", UUID.randomUUID(), 0, 5);
    
    // when
    boolean isExpired = pass.isExpired();

    // then
    assertTrue(isExpired);
  }

  @Test
  void should_retTrue_whenPassHasUsesLeft() {
    // given
    Pass pass = new Pass(UUID.randomUUID(), "John", "Jacob", "jingleheimer@smith.com", UUID.randomUUID(), 1, 5);
    
    // when
    boolean isExpired = pass.isExpired();

    // then
    assertFalse(isExpired);
  }

  @Test
  void should_retTrue_whenPassUsedUpSuccessfully() {
    // given
    int passUses = 5;
    Pass pass = new Pass("John", "Jacob", "jingleheimer@smith.com", UUID.randomUUID(), passUses);
    
    // when
    boolean isExpiredInitially = pass.isExpired();
    List<Boolean> usages = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      usages.add(pass.use());
    }
    boolean isExpiredAfterwards = pass.isExpired();
    boolean canStillUse = pass.use();

    // then
    assertAll(
      () -> assertFalse(isExpiredInitially),
      () -> assertTrue(isExpiredAfterwards),
      () -> assertFalse(usages.contains(false)),
      () -> assertFalse(canStillUse)
    );
  }
}