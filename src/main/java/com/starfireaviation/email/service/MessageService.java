/*
 *  Copyright (C) 2022 Starfire Aviation, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.starfireaviation.email.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.starfireaviation.model.Event;
import com.starfireaviation.model.EventType;
import com.starfireaviation.model.Message;
import com.starfireaviation.model.Question;
import com.starfireaviation.model.User;
import com.starfireaviation.email.config.ApplicationProperties;
import com.starfireaviation.email.util.TemplateUtil;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;

/**
 * MessageService.
 */
@Slf4j
public class MessageService {

    /**
     * TEMPLATE_LOCATION.
     */
    private static final String TEMPLATE_LOCATION = "/templates";

    /**
     * FreeMarker Configuration.
     */
    private final Configuration freemarkerConfig;

    /**
     * ApplicationProperties.
     */
    private final ApplicationProperties applicationProperties;

    /**
     * EmailService.
     *
     * @param aProps ApplicationProperties
     * @param config Configuration
     */
    public MessageService(final ApplicationProperties aProps,
                        final Configuration config) {
        applicationProperties = aProps;
        freemarkerConfig = config;
    }

    /**
     * Sends a message for user deletion.
     *
     * @param message Message
     */
    public void sendUserDeleteMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            send(
                    user.getId(),
                    applicationProperties.getFromAddress(),
                    user.getEmail(),
                    null,
                    null,
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("user_delete_subject.ftl"),
                            TemplateUtil.getModel(user, null, null, applicationProperties)),
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("user_delete_body.ftl"),
                            TemplateUtil.getModel(user, null, null, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for quiz completion.
     *
     * @param message message
     */
    public void sendQuizCompleteMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            send(
                    user.getId(),
                    applicationProperties.getFromAddress(),
                    user.getEmail(),
                    null,
                    null,
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("quiz_complete_subject.ftl"),
                            TemplateUtil.getModel(user, null, null, applicationProperties)),
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("quiz_complete_body.ftl"),
                            TemplateUtil.getModel(user, null, null, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message to RSVP for an upcoming event.
     *
     * @param message Message
     */
    public void sendEventRSVPMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Event event = getEvent(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(
                        user.getId(),
                        applicationProperties.getFromAddress(),
                        user.getEmail(),
                        null,
                        null,
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_rsvp_subject.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)),
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_rsvp_body.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for an upcoming event.
     *
     * @param message Message
     */
    public void sendEventUpcomingMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Event event = getEvent(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(
                        user.getId(),
                        applicationProperties.getFromAddress(),
                        user.getEmail(),
                        null,
                        null,
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_upcoming_subject.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)),
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_upcoming_body.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message to a user that an event has started.
     *
     * @param message Message
     */
    public void sendEventStartMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Event event = getEvent(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(
                        user.getId(),
                        applicationProperties.getFromAddress(),
                        user.getEmail(),
                        null,
                        null,
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_start_subject.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)),
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_start_body.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message that a question has been asked.
     *
     * @param message Message
     */
    public void sendQuestionAskedMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Question question = getQuestion(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            send(
                    user.getId(),
                    applicationProperties.getFromAddress(),
                    user.getEmail(),
                    null,
                    null,
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("question_subject.ftl"),
                            TemplateUtil.getModel(user, null, question, applicationProperties)),
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("question_body.ftl"),
                            TemplateUtil.getModel(user, null, question, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for registering for an upcoming event.
     *
     * @param message Message
     */
    public void sendEventRegisterMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Event event = getEvent(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(
                        user.getId(),
                        applicationProperties.getFromAddress(),
                        user.getEmail(),
                        null,
                        null,
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_register_subject.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)),
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_register_body.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for unregistering from an upcoming event.
     *
     * @param message Message
     */
    public void sendEventUnregisterMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Event event = getEvent(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(
                        user.getId(),
                        applicationProperties.getFromAddress(),
                        user.getEmail(),
                        null,
                        null,
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_unregister_subject.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)),
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_unregister_body.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for user settings verified.
     *
     * @param message Message
     */
    public void sendUserSettingsVerifiedMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            send(
                    user.getId(),
                    applicationProperties.getFromAddress(),
                    user.getEmail(),
                    null,
                    null,
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("user_settings_verified_subject.ftl"),
                            TemplateUtil.getModel(user, null, null, applicationProperties)),
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("user_settings_verified_body.ftl"),
                            TemplateUtil.getModel(user, null, null, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for user settings changed.
     *
     * @param message Message
     */
    public void sendUserSettingsChangeMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            send(
                    user.getId(),
                    applicationProperties.getFromAddress(),
                    user.getEmail(),
                    null,
                    null,
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("user_verify_settings_subject.ftl"),
                            TemplateUtil.getModel(user, null, null, applicationProperties)),
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("user_verify_settings_body.ftl"),
                            TemplateUtil.getModel(user, null, null, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends an invitation.
     *
     * @param message Message
     */
    public void sendInviteMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Event event = getEvent(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(
                        user.getId(),
                        applicationProperties.getFromAddress(),
                        "", // TODO
                        null,
                        null,
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_invite_subject.ftl"),
                                TemplateUtil.getModel(user, null, null, applicationProperties)),
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_invite_body.ftl"),
                                TemplateUtil.getModel(user, null, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a password reset message.
     *
     * @param message Message
     */
    public void sendPasswordResetMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            send(
                    user.getId(),
                    applicationProperties.getFromAddress(),
                    user.getEmail(),
                    null,
                    null,
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("password_reset_subject.ftl"),
                            TemplateUtil.getModel(user, null, null, applicationProperties)),
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("password_reset_body.ftl"),
                            TemplateUtil.getModel(user, null, null, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Resends a message for user settings changed.
     *
     * @param message Message
     */
    public void resendUserSettingsChangeMsg(final Message message) {
        // Not implemented
    }

    /**
     * Receives a message and returns response.
     *
     * @param message Message
     * @return response
     */
    public String receiveMessage(final Message message) {
        log.info(String.format("receiveMessage() message received was [%s]", message));
        return null;
    }

    /**
     * Sends a message to display information.
     *
     * @param message Message
     */
    public void sendDisplayMsg(final Message message) {
        // Not implemented
    }

    /**
     * Sends a last minute message to register/RSVP for an upcoming event.
     *
     * @param message Message
     */
    public void sendEventLastMinRegistrationMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Event event = getEvent(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(
                        user.getId(),
                        applicationProperties.getFromAddress(),
                        user.getEmail(),
                        null,
                        null,
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_last_min_registration_subject.ftl"),
                                TemplateUtil.getModel(user, null, null, applicationProperties)),
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_last_min_registration_body.ftl"),
                                TemplateUtil.getModel(user, null, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message to a user that an event has completed.
     *
     * @param message Message
     */
    public void sendEventCompletedMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
    }

    /**
     * Sends an email.
     *
     * @param userId      user ID
     * @param fromAddress from address
     * @param toAddress   to address
     * @param ccAddress   cc address
     * @param bccAddress  bcc address
     * @param subject     subject
     * @param body        body
     */
    private void send(
            final Long userId,
            final String fromAddress,
            final String toAddress,
            final String ccAddress,
            final String bccAddress,
            final String subject,
            final String body) {
        final String msg = String.format(
                "Sending... fromAddress [%s]; toAddress [%s]; ccAddress [%s]; bccAddress [%s]; subject [%s]; body [%s]",
                fromAddress,
                toAddress,
                ccAddress,
                bccAddress,
                subject,
                body);
        log.info(msg);
        try {
            final Email from = new Email(fromAddress);
            final Email to = new Email(toAddress);
            final Content content = new Content("text/html", body);
            final Mail mail = new Mail(from, subject, to, content);

            final SendGrid sg = new SendGrid(applicationProperties.getSendGridApiKey());
            final Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    private Event getEvent(final Message message) {
        return null;
    }

    private User getUser(final Message message) {
        return null;
    }

    private Question getQuestion(final Message message) {
        return null;
    }

}
