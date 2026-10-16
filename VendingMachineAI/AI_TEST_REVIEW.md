# AI Test Generation Review
•	What was the overall quality of the generated tests? What did the AI do particularly well or poorly?
    with the original test generation it did not properly diagnose generate proper tests for all the files nor test the functionality of the logic within all tests, and therein missed the bug in the vendingmachine file. what it did do well was give proper coverage in the files that it did generate / account for
•	How did the AI-generated test suite compare with your manually designed suite from Project 2/Task 1 in terms of coverage and test quality?
    I would imagine that the Ai had better quality / coverage, but only after the human interaction / modification of the prompting
•	Did coverage-guided prompting improve the generated suite? Give at least one concrete example.
    yes, as originally the generated suite did not create any vendingMachineException tests, and after prompting it did create them and properly covered them
•	Did the AI generate any incorrect, redundant, weak, or misleading assertions? How did you recognize them?
    none that I could tell, but if there was I would recognize them by going through the outputted tests and results and noticing that the same test or parameters were being run multiple times 
•	What does this exercise suggest about the strengths and limitations of using AI to generate software tests?
    this shows that while ai can be useful in generating a broad coverage, human intervention is still neccesary in order to finalize and otherwise properly implement all testing 