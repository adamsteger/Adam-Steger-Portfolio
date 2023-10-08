package com.capstone.lifesabit.gateguard.notifications;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.websocket.EncodeException;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.capstone.lifesabit.gateguard.SQLLinker;
import com.capstone.lifesabit.gateguard.login.Session;
import com.capstone.lifesabit.gateguard.login.SessionManager;
import com.capstone.lifesabit.gateguard.login.websocket.WebsocketController;
import com.capstone.lifesabit.gateguard.login.websocket.WebsocketTypes;
import com.capstone.lifesabit.gateguard.passes.Pass;
import com.capstone.lifesabit.gateguard.settings.UserSettings;

/*
 * Class to automatically check for expiration on date-based passes
 */
@ComponentScan(basePackages = "com.capstone.lifesabit.gateguard.notifications")
@Component
public class PassExpirationNotifier implements DisposableBean {

  ScheduledThreadPoolExecutor tpe = new ScheduledThreadPoolExecutor(1);

  /*
   * Sets the time interval to recheck for updates
   */
  public PassExpirationNotifier() {
    tpe.schedule(new ScanForExpiringPassesRunnable(tpe), 1, TimeUnit.DAYS);
  }

  @Override
  public void destroy() {
    tpe.shutdownNow();
  }

  class ScanForExpiringPassesRunnable implements Runnable {

    ScheduledThreadPoolExecutor tpe = null;

    public ScanForExpiringPassesRunnable(ScheduledThreadPoolExecutor tpe) {
        this.tpe = tpe;
    }

    /*
     * The actual code being run every interval
     */
    public void run() {
        tpe.schedule(new ScanForExpiringPassesRunnable(tpe), 1, TimeUnit.DAYS);

        // Sets a future date to 5 days in advance for date-based pass expires soon notifications
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 5);
        Long future = cal.getTimeInMillis();


        ArrayList<Pass> passes = SQLLinker.getInstance().loadPasses();
        for (Pass pass : passes) {
            UserSettings userSettings = SQLLinker.getInstance().loadUserSettings(pass.getUserID().toString());
            // Pass expires soon notification check
            if (userSettings.getNotifPassExpiresSoon() && !pass.getUsageBased() && pass.getExpirationDate().compareTo(future) < 0 && !pass.getExpired()) {
                String title = pass.getFirstName() + " " + pass.getLastName() + "'s pass expires soon!";
                Notification notif = new Notification(title, pass.getPassID(), pass.getUserID(), Instant.now().toEpochMilli(), NotificationType.PASS_EXPIRES_SOON, "");
                SQLLinker.getInstance().addNotification(notif);
                Session userSession = SessionManager.getSession(SQLLinker.getInstance().getMemberByUUID(pass.getUserID().toString()));
                if (userSession != null) {
                    try {
                        WebsocketController.sendMessage(WebsocketTypes.NOTIFICATION, new com.capstone.lifesabit.gateguard.login.websocket.object.Notification(notif), 
                        userSession.getSessionKey());
                    } catch (IOException | EncodeException e) {
                        e.printStackTrace();
                    }
                }
            // Pass expired notification check
            } else if (userSettings.getNotifPassExpiration() && !pass.getUsageBased() && pass.isExpired()) {
                SQLLinker.getInstance().editPass(pass.getPassID(), pass.getFirstName(), pass.getLastName(), pass.getEmail(), pass.getExpired(), pass.getUsageBased(), pass.getExpirationDate()); 
                String title = pass.getFirstName() + " " + pass.getLastName() + "'s pass is expired!";
                Notification notif = new Notification(title, pass.getPassID(), pass.getUserID(), Instant.now().toEpochMilli(), NotificationType.PASS_EXPIRED, "");
                SQLLinker.getInstance().addNotification(notif);
                Session userSession = SessionManager.getSession(SQLLinker.getInstance().getMemberByUUID(pass.getUserID().toString()));
                if (userSession != null) {
                    try {
                        WebsocketController.sendMessage(WebsocketTypes.NOTIFICATION, new com.capstone.lifesabit.gateguard.login.websocket.object.Notification(notif), 
                        userSession.getSessionKey());
                    } catch (IOException | EncodeException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
  }
}