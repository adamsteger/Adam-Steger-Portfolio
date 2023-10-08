package com.capstone.lifesabit.gateguard.login;

import java.util.HashMap;
import java.util.UUID;

import com.capstone.lifesabit.gateguard.EmailSender;
import com.capstone.lifesabit.gateguard.SQLLinker;

public class PasswordResetManager {
  
  // This maps the password reset request ID -> the user ID
  private static HashMap<UUID, UUID> resetRequestMap = new HashMap<>();

  public static UUID requestReset(UUID memberID) {
    UUID resetUUID = UUID.randomUUID();
    resetRequestMap.put(resetUUID, memberID);
    return resetUUID;
  }

  public static UUID getMemberUUIDFromResetID(UUID resetID) {
    return resetRequestMap.get(resetID);
  }

  public static boolean isValidResetID(UUID resetID) {
    return resetRequestMap.containsKey(resetID);
  }

  public static boolean resetPassword(UUID resetUUID, String hashedPassword) {
    // If that ID matches one we have in the map
    if (isValidResetID(resetUUID)) {
      UUID memberUUID = getMemberUUIDFromResetID(resetUUID);
      // If we were able to successfully update the user's password
      if (SQLLinker.getInstance().updatePassword(memberUUID, Member.saltPassword(hashedPassword))) {
        // Send them an email
        EmailSender.getInstance().sendPasswordResetNotifyEmail(SQLLinker.getInstance().getMemberByUUID(memberUUID.toString()));
        // Remove them from the map since their password was reset successfully
        resetRequestMap.remove(resetUUID);

        return true;
      }
    }
    return false;
  }
}
