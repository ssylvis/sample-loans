## Sample Project

This project was done as part of a three-hour coding challenge and is included here as a kind of coding showcase. The
constraints (time and input) of the challenge allow for a pretty simple algorithm, so this is mostly a showcase of
class design and coding style.

### Background
* We borrow money from our banking partners through debt facilities. In turn, we use these facilities to extend loans
to customers.
* A banking partner may require a covenant, which is a set of restrictions on the loans that a facility may fund. Common
restrictions include:
    * Maximum default rate: A bank may restrict us from funding certain riskier loans.
    * Geographic location: A bank may restrict us from funding loans in certain states.
* We may establish multiple facilities with a single bank. In this case, the bank may define covenants that apply to all
of their facilities, or only to an individual one.

### Goals
You will be provided with a list of facilities and covenants, as well as a stream of loans that we would like to fund
with those facilities. Your task is to write a program that consumes loans from the stream and assigns each loan to a
facility while respecting each facility’s covenants.

### Input
An input data set will consist of four CSV files, describing the facilities, banks, covenants, and loans, respectively.
These files are described in the following sections. You will be given two data sets, a small data set (in the folder
‘small’) for manually verifying your understanding of the problem, along with a large data set (in the folder ‘large’)
for more rigorously stress­testing your program. The folder ‘small’ will also contain the solution files
‘assignments.csv’ and ‘yields.csv’. These files will be described later.

### Improvements

We could make more optimal decisions for each loan if we were permitted to assign loans in batch rather than real-time
procesing (at the cost of delaying individual loan assignment). This would be a variation of the knapsack problem where
we are trying to find the assignment of the set of loans that would minimize the cost (or maximize the expected yield)
within a given batch.
