function parseJsonFile(json) {	    	
	return json.candidates;
}	

function parseResults(json, candidates) {
	arrayLength = json.admissionResults.length;
	for (var i = 0; i < arrayLength; i++) {
		var candidateId = json.admissionResults[i].candidateId;
		var finalGrade = json.admissionResults[i].finalGrade;
		var resultState = json.admissionResults[i].admissionStatus.statusString;

		// Add result to candidate
		for (var j = 0; j < candidates.length; j++) {
			if (candidates[j].id == candidateId) {
				candidates[j].finalGrade = finalGrade;
				candidates[j].resultState = resultState;				
				break;
			}
		}
	}

	return candidates;
}

function editCandidateCommit(values, candidates) {		
	cand[0].firstName = values.firstName;
	cand[0].lastName = values.lastName;
	cand[0].gpaGrade = values.gpaGrade;
	cand[0].ATestGrade = values.ATestGrade;
	cand[0].socialId = values.socialId;
}

function deleteUser(id) {
	cand[id] = null;
}