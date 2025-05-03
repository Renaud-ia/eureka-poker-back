Evaluate how well a code base implements DDD tactical patterns and supple design principles on a scale of 1-10, where:

1 = Pattern/principle poorly implemented or necessary implementations are missing
10 = Pattern/principle perfectly implemented and on purpose

## DDD Tactical Patterns

### Value Objects (1-10)
Rate how well the code implements immutable objects that model concepts with no identity.
Consider:
- For found value objects:
  - Are value objects immutable?
  - Do they encapsulate related attributes?
  - Are they equality-comparable by their attributes?
  - Do they have validation logic implemented as assertions?
  - Do they provide basic operations as methods (closure of operations)? 
- Are there primitive types that are used instead of value objects?

### Entities (1-10)
Rate how well the code implements objects with proper identity.
Consider:
- Is identity clearly defined and maintained?
- Are entities mutable only through well-defined operations?
- Is business logic properly encapsulated?
- Are entity boundaries clear?

### Aggregates (1-10)
Rate how well the code defines consistency boundaries.
Consider:
- Are aggregate roots clearly identified?
- Is invariant protection enforced?
- Are aggregate boundaries respected?
- Is persistence managed through the root?

### Domain Events (1-10)
Rate how well the code captures and handles important domain occurrences.
Consider:
- Are domain events used to capture important changes?
- Is event handling properly separated?
- Are events immutable and well-documented?
- Is event sourcing used where appropriate?

### Repositories (1-10)
Rate how well the code manages persistence concerns.
Consider:
- Do repositories abstract persistence details?
- Is collection-like access provided?
- Are repository interfaces cohesive?
- Is persistence logic properly encapsulated?

### Domain Services (1-10)
Rate how well the code handles operations that don't belong to entities.
Consider:
- Are domain services stateless?
- Do they encapsulate complex operations?
- Are they focused on domain concepts?
- Do they coordinate multiple entities?


## Supple Design Principles

### Intention-Revealing Interfaces (1-10)
Rate how well the code communicates its purpose.
Consider:
- Are names clear and meaningful?
- Do interfaces reveal their purpose?
- Is behavior obvious from signatures?
- Is documentation needed for understanding?

### Side-Effect-Free Functions (1-10)
Rate how well the code separates queries from commands.
Consider:
- Are queries free of side effects?
- Is command-query separation maintained?
- Are operations predictable?
- Is state mutation explicit?

### Assertions (1-10)
Rate how well the code maintains invariants.
Consider:
- Are preconditions checked?
- Are postconditions verified?
- Are invariants protected?
- Is validation comprehensive?

### Standalone Classes (1-10)
Rate how well the code minimizes dependencies.
Consider:
- Are classes loosely coupled?
- Can classes be understood in isolation?
- Are dependencies explicit?
- Is composition favored over inheritance?

### Closure of Operations (1-10)
Rate how well the code maintains type safety and composability.
Consider:
- Do operations return the same types they accept?
- Are operations chainable?
- Is type safety maintained?
- Are operations composable?

## Overall Assessment
Calculate separate scores for:
1. DDD Tactical Patterns (average of 6 pattern scores)
2. Supple Design Principles (average of 5 principle scores)

For each pattern/principle, provide:
- Numerical rating (1-10)
- Brief justification for the rating
- Specific examples of good/poor implementation
- Suggestions for improvement with examples
- Positive aspects of the current design

Please highlight specific code examples that demonstrate adherence to or violation of each pattern/principle.

## Summary
Provide an overview of:
- Major strengths in the codebase
- Areas needing improvement with examples of the problems
- Priority recommendations with class names
- Overall assessment of the DDD implementation

