// Control script for matching the data entered in the password and confirm password fields
function validatePassword() {
	var password = document.getElementById("password");
	var confirmPassword = document.getElementById("confirmPassword");

	// Lunghezza minima
	if (password.length < 8 || confirmPassword.length < 8) {
		alert("La password deve essere lunga almeno 8 caratteri.");
		//event.preventDefault(); // Blocca l'invio del form
		return false;
	}

	// Controlla se le password corrispondono
	if (password.value !== confirmPassword.value) {
		alert("Le password non coincidono!");
		return false; // Impedisce l'invio del modulo
	}

	return true; // Permette l'invio del modulo
}