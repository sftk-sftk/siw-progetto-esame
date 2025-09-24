// Mostra/nascondi input nuova tipologia
function toggleNewTipologiaInput() {
	const select = document.getElementById('tipologieSelect');
	const newTipologiaDiv = document.getElementById('newTipologiaDiv');
	const newTipologiaInput = document.getElementById('nomeTipologia');

	if (select.value === '__new__') {
		newTipologiaDiv.style.display = 'block';
		newTipologiaInput.required = true;
	} else {
		newTipologiaDiv.style.display = 'none';
		newTipologiaInput.required = false;
		newTipologiaInput.value = '';
	}
}

// Filtra prodotti affini per nome o tipologia
function filterProdottiAffini() {
	const filtro = document.getElementById('filtroProdotti').value.toLowerCase();
	const prodottiDiv = document.getElementById('prodottiList');
	const prodotti = prodottiDiv.querySelectorAll('div');

	prodotti.forEach(div => {
		const label = div.querySelector('label');
		const text = label.textContent.toLowerCase();
		if (text.includes(filtro)) {
			div.style.display = 'block';
		} else {
			div.style.display = 'none';
		}
	});
}

// Inizializza il campo nuova tipologia se necessario (es. quando si ricarica con errori)
window.onload = function() {
	toggleNewTipologiaInput();

	// Se vuoi impostare visibilità dinamica in base a un valore lato server, potresti aggiungere logica qui
};

document.getElementById('formNuovoProdotto').addEventListener('submit', function(event) {
	const prezzoInput = document.getElementById('prezzo');
	const tipologiaSelect = document.getElementById('tipologieSelect');
	const nuovaTipologiaInput = document.getElementById('nomeTipologia');

	let valid = true;
	let messaggiErrore = [];

	// Controllo prezzo
	const prezzo = parseFloat(prezzoInput.value);
	if (isNaN(prezzo) || prezzo <= 0) {
		valid = false;
		messaggiErrore.push("Il prezzo deve essere un numero maggiore di 0.");
	}

	// Controllo tipologia selezionata
	if (tipologiaSelect.value === '') {
		valid = false;
		messaggiErrore.push("Seleziona una tipologia dal menu a tendina.");
	}

	// Se è selezionata una nuova tipologia, il campo deve essere compilato
	if (tipologiaSelect.value === '__new__') {
		const nuovaTipologia = nuovaTipologiaInput.value.trim();
		if (nuovaTipologia === '') {
			valid = false;
			messaggiErrore.push("Inserisci il nome della nuova tipologia.");
		}
	}

	// Se ci sono errori, blocca l'invio del form e mostra gli errori
	if (!valid) {
		event.preventDefault();

		// Mostra errori in alert o crea un contenitore per mostrarli nel DOM
		alert(messaggiErrore.join('\n'));

		// In alternativa, mostra gli errori dentro una <div id="erroriForm"></div>
		/*
		const erroriDiv = document.getElementById('erroriForm');
		if (erroriDiv) {
			erroriDiv.innerHTML = '<ul><li>' + messaggiErrore.join('</li><li>') + '</li></ul>';
		}
		*/
	}
});