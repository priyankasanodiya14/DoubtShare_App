async function getAllDoubts() {
    const response = await fetch('/doubts/all');
    const doubts = await response.json();
    document.getElementById('result').innerText = JSON.stringify(doubts, null, 2);
}

async function getDoubtById() {

    var token = localStorage.getItem("jwtToken");

    const doubtId = document.getElementById('doubtIdInput').value;
    const response = await fetch(`https://revlyassignment-production.up.railway.app/tutors/tutor-solve-doubt/${doubtId}/`,{
        headers : {
            "Authorization" : `Bearer ${token}`
        }
    });
    
    try {
        const doubt = await response.json();
        document.getElementById('result').innerText = JSON.stringify(doubt, null, 2);
    } catch (error) {
        document.getElementById('result').innerText = 'Doubt not found.';
    }
}

async function getDoubtsBySubject() {
    const subject = document.getElementById('subjectInput').value;
    const response = await fetch(`/doubts/by-subject/${subject}`);
    const doubts = await response.json();
    document.getElementById('result').innerText = JSON.stringify(doubts, null, 2);
}
