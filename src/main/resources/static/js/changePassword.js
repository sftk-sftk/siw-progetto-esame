function validateForm(event) {
	const newPassword = document.getElementById("newPassword").value;
	const confirmPassword = document.getElementById("confirmPassword").value;

	// Lunghezza minima
	if (newPassword.length < 8 || confirmPassword.length < 8) {
		alert("La nuova password deve essere lunga almeno 8 caratteri.");
		event.preventDefault(); // Blocca l'invio del form
		return false;
	}

	// Nuova password uguale a conferma
	if (newPassword !== confirmPassword) {
		alert("Nuova Password e Conferma Nuova Password non coincidono.");
		event.preventDefault();
		return false;
	}

	// Tutto OK
	return true;
}