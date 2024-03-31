package edu.java.scrapper.httpClients.gitHub;

import java.net.URI;
import lombok.Getter;

@SuppressWarnings("checkstyle:MultipleStringLiterals")
@Getter
public enum GitHubEventType {
    PUSH_EVENT("PushEvent"),
    ISSUES_EVENT("IssuesEvent"),
    ISSUE_COMMENT_EVENT("IssueCommentEvent"),
    PULL_REQUEST_EVENT("PullRequestEvent"),
    PULL_REQUEST_REVIEW_EVENT("PullRequestReviewEvent"),
    FORK_EVENT("ForkEvent");

    private final String type;

    GitHubEventType(String type) {
        this.type = type;
    }

    //вообще, наверное в идеале этот процесс должен происходить на стороне бота,
    //а скраппер должен просто отправить дто с данными, но пока так,
    // чтобы вписаться в существующую систему с минимальными изменениями
    public static String getGitHubEventMessage(GitHubEventDto dto, URI uri) {
        String user = "User **" + dto.actor().login() + "**";

        StringBuilder message = new StringBuilder();
        message.append(user).append("\n");

        switch (dto.type()) {
            case "PushEvent" -> {
                String branch = dto.payload().ref();
                branch = branch.substring(branch.lastIndexOf("/"));
                message.append("Pushed new commits into ")
                    .append(branch)
                    .append(" branch")
                    .append("\n")
                    .append("Update time: ")
                    .append(dto.updateTime());
            }
            case "IssuesEvent" -> {
                String url = dto.payload().issue().url();
                String title = dto.payload().issue().title();

                if (dto.payload().action().equals("opened")) {
                    message.append("Created new issue ")
                        .append(createMdHyperlink(url, title))
                        .append("\n")
                        .append("Update time: ")
                        .append(dto.updateTime());
                } else if (dto.payload().action().equals("closed")) {
                    message.append("Issue ")
                        .append(createMdHyperlink(url, title))
                        .append(" was solved")
                        .append("\n")
                        .append("Update time: ")
                        .append(dto.updateTime());
                } else {
                    message.append("Changes in issue ")
                        .append(createMdHyperlink(url, title))
                        .append("\n")
                        .append("Update time: ")
                        .append(dto.updateTime());
                }
            }
            case "IssueCommentEvent" -> {
                String url = dto.payload().issue().url();
                String title = dto.payload().issue().title();
                message.append("Commented ")
                    .append(createMdHyperlink(url, title))
                    .append("\n")
                    .append("Update time: ")
                    .append(dto.updateTime());
            }
            case "PullRequestEvent" -> {
                if (dto.payload().action().equals("opened")) {
                    message.append("Created new PullRequest ")
                        .append(dto.payload().pullRequest().title())
                        .append("\n")
                        .append("Update time: ")
                        .append(dto.updateTime());
                } else {
                    String title = dto.payload().pullRequest().title();
                    String url = dto.payload().pullRequest().url();
                    message.append("PullRequest ")
                        .append(createMdHyperlink(url, title))
                        .append(" was closed")
                        .append("\n")
                        .append("Update time: ")
                        .append(dto.updateTime());
                }
            }
            case "PullRequestReviewEvent" -> {
                String title = dto.payload().pullRequest().title();
                String url = dto.payload().pullRequest().url();
                message.append("Started reviewing PullRequest ")
                    .append(createMdHyperlink(url, title))
                    .append("\n")
                    .append("Update time: ")
                    .append(dto.updateTime());
            }
            case "ForkEvent" -> message.append("Has forked repository");

            default -> message.append("performed an unknown action");
        }

        message.append("\nInto repo ").append(createMdHyperlink(uri.toString(), dto.repo().name()));

        return message.toString();
    }

    private static String createMdHyperlink(String url, String title) {
        return "[" + title + "](" + url + ")";
    }
}
