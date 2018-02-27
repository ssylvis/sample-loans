## Sample Project

This project was done as part of a three-hour coding challenge and is included here as a kind of coding showcase. The
constraints (time and input) of the challenge allow for a pretty simple algorithm, so this is mostly a showcase of
coding style.

See _Coding Challenge Readme_ doc for a detailed description of the coding challenge.

### Improvements

We could make more optimal decisions for each loan if we were permitted to assign loans in batch rather than real-time
procesing (at the cost of delaying individual loan assignment). This would be a variation of the knapsack problem where
we are trying to find the assignment of the set of loans that would minimize the cost (or maximize the expected yield)
within a given batch.
