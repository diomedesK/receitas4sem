function populateRecipesFromAPI( url, targetContainer ){
	fetch(url)
		.then(response => response.json())
		.then(data => {

			data.forEach(recipe => {
				console.log(recipe);

				const recipeBlock = document.createElement('div');
				recipeBlock.classList.add("recipe-card");

						recipeBlock.innerHTML = `
						  <a href="/recipes/${recipe.id}">
							  <h3>${recipe.name}</h3>
						  </a>
						  <p class="no-margin">${recipe.description}</p>
						  <p class="no-margin"> ${recipe.prepareInMinutes} minutos 🕛 </p>
						  <p class="no-margin"> ${recipe.accessesWithinLast7Days} acesso(s) recentes 👁️ </p>

						 <div>
						   ${recipe.categories.slice(0, 3).map((category, index) => `${category}${index !== recipe.categories.length - 1 ? ', ' : ''}`).join('')}
						  </div>
					`;

				targetContainer.appendChild(recipeBlock);

			});
		})
		.catch(error => {
			console.error('Error fetching recipe data:', error);
		});

}

function populateRecipesFromURL() {
	const url = new URL(window.location.href);
	const pathname = url.pathname;

	if (pathname === '/busca' && url.searchParams.has('q')) {
		const searchText = url.searchParams.get('q');

		const targetContainer = document.getElementById("recipe-container");
		targetContainer.innerHTML = '';

		document.getElementById("resultSetTitle").innerHTML = `RESULTADOS PARA "${searchText.toUpperCase()}"`;
		document.getElementById("resultSetDescription").innerHTML = ``;


		populateRecipesFromAPI(`/api/recipes?name=${encodeURIComponent(searchText)}`, targetContainer);
		return true;
	}

	return false;
}

window.populateRecipesFromURL = populateRecipesFromURL
window.populateRecipesFromAPI = populateRecipesFromAPI
