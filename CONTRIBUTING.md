# Contributing to Named Binary Tag (Named-Binary-Tag)

Thank you for your interest in contributing! We welcome contributions that improve functionality, fix bugs, or enhance documentation. To ensure consistency, please follow these guidelines:

## Code Style

- **Language:** Java 17 (source/target).
- **Formatting:** Follow standard conventions. We recommend using [Google Java Style](https://google.github.io/styleguide/javaguide.html) or similar.
- **Dependencies:** Only use existing project dependencies (none external except test libs).
- **Imports:** Organize imports (you may use IDE (recommended JetBrain's Intellij IDEA) auto-format).

## Development Setup

1. **Fork** the repository and clone your fork.
2. Create a new branch for your feature/bugfix: `git checkout -b feature-name`.
3. Ensure your code compiles and tests pass locally:
   ```bash
   mvnw clean compile
   mvnw test
   ```

## Writing Code

- **Naming:** Follow existing naming (e.g., tags named `FooTag`, type `FOO`).
- **Clone Methods:** Ensure `clone()` creates a deep copy (copy arrays/lists).
- **Serialization:** Use `DataOutput/Input` as in other tags. Always write tag ID and name.
- **Error Handling:** Throw `IOException` on I/O errors; validate inputs where needed.
- **Null Checks:** Tag names and values should not be null (method contracts use `@NotNull`).

## Testing

- **Unit Tests:** Add tests in `src/test/java`. We use JUnit 5.
- **Coverage:** Write tests for new behaviors, and edge cases (e.g., empty lists, invalid input).
- **Run Tests:** Always run `mvnw test` to ensure nothing breaks.
- **Examples:** For new features, consider adding an example in `src/test/java/Example*.java`.

## Pull Requests

- Submit PRs to the `main` branch. Clearly describe changes.
- If fixing a bug, reference it in the PR description.
- **Review Process:** We will review PRs and may request changes. Ensure your branch is up-to-date.
- **Commits:** Keep commits focused and atomic. Amend or squash when needed.

## CI / Quality Checks

We use GitHub Actions to automatically run tests and checks on PRs. Ensure your changes pass all checks.

## License / CLA

By contributing, you agree that your contributions are licensed under the LGPL v3.0 (same as this project) without additional restrictions.

Thank you for improving **Named Binary Tag**!