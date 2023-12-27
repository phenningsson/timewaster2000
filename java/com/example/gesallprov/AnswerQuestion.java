package com.example.gesallprov;

// hjälpklass till QuizFragment-klassen. Här skapas quizzens frågor, svarsalternativ och rätt svar
//hämtas genom att AnswerQuestion anropas i QuizFragment-klassen

public class AnswerQuestion {

    // frågorna som ställs, skapas här
    public static String[] question = {
            //första frågan
            "In a website browser address bar, what does “www” stand for?",
            // andra frågan
            "What is the capital of Liechtenstein?",
            // tredje frågan, osv
            "In what year was Lord of the Rings published?",
            "Which country consumes the most chocolate per capita?",
            "What is the largest country in the world?",
            "What is the most populous country in the world?",
            "What is the most populous city in the world?",
            "Which blood type is a universal donor?",
            "How many pounds are in a ton?",
            "What percentage of the Earth’s wildlife is found in the ocean?"
    };

    //svarsalternativen till frågorna, skapas här
    public static String[][] choices = {
            // svarsalternativen till första frågan
            {"We Were Web", "World Wide We", "World Wide Web", "Web World Wide"},
            // svarsalternativen till andra frågan
            {"Vaduz", "Freiburg", "Ulm", "Koblenz"},
            // svarsalternativen till tredje frågan, osv
            {"1964", "1954", "1946", "1945"},
            {"Sweden", "Switzerland", "Austria", "Belgium"},
            {"USA", "China", "Russia", "Canada"},
            {"China", "India", "Russia", "USA"},
            {"Chongqing", "Delhi", "Tokyo", "Shanghai"},
            {"O-Negative", "A-Negative", "B-Positive", "AB-Negative"},
            {"1000", "2000", "1500", "2500"},
            {"52", "94", "72", "33"}
    };

    // rätt svar på frågorna, skapas här
    public static String[] correctAnswers = {
            // rätt svar på första frågan
            "World Wide Web",
            // rätt svar på andra frågan
            "Vaduz",
            // rätt svar på tredje frågan, osv
            "1954",
            "Switzerland",
            "Russia",
            "China",
            "Tokyo",
            "O-Negative",
            "2000",
            "94"
    };

}
