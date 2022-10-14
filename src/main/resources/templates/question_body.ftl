<html>
    <body>
        <h2>${questionText}</h2>
        <p>Answers:</p>
        <ol>
            <li><a href="${hostapp}/questions/${questionId}/answer/${userId}/${answerChoice1}">${answerChoice1}</a>: ${answerText1}</li>
            <li><a href="${hostapp}/questions/${questionId}/answer/${userId}/${answerChoice2}">${answerChoice2}</a>: ${answerText2}</li>
            <li><a href="${hostapp}/questions/${questionId}/answer/${userId}/${answerChoice3}">${answerChoice3}</a>: ${answerText3}</li>
            <li><a href="${hostapp}/questions/${questionId}/skip/${userId}">SKIP</a></li>
        </ol>
        <p>${referenceMaterial}</p>
    </body>
</html>
