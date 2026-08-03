package fedoseev.tasks.system.security;

public record LoginRequest(
        String username,
        String password
) {}