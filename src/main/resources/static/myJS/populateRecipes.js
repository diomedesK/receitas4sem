function populateRecipesFromAPI( url, targetContainer ){
	fetch(url)
		.then(response => response.json())
		.then(data => {

			data.forEach(recipe => {
				const recipeBlock = document.createElement('div');
				recipeBlock.classList.add("recipe-card");

						recipeBlock.innerHTML = `
						  <a href="/receitas/${recipe.id}">
							  <h3>${recipe.name}</h3>
						  </a>
						  <p class="no-margin">${recipe.description}</p>
						  <p class="no-margin"> ${recipe.prepareInMinutes} minutos 🕛 </p>
						  <p class="no-margin"> ${recipe.accessesWithinLast7Days} acesso(s) recentes 👁️ </p>
						  <p class="no-margin"> ${recipe.averageRating} ⭐</p>

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

function populateRecipesFromURLParams() {
	const url = new URL(window.location.href);
	const pathname = url.pathname;

	if (pathname === '/busca' && url.searchParams.has('q')) {
		let targetQuery = ""
		let resultPlaceholderText = ""

		const searchText = url.searchParams.get('q').toLowerCase();
		const ingredientQuery = searchText.match(/\bingrediente:([\w:]+)/);

		if ( ingredientQuery != null && ingredientQuery[1] != null ){
			targetQuery = `api/receitas?ingrediente=${ingredientQuery[1]}`
			resultPlaceholderText = `ingrediente:${ingredientQuery[1]}`.toUpperCase();
		} else {
			targetQuery = `/api/receitas?nome=${encodeURIComponent(searchText)}`
			resultPlaceholderText = searchText.toUpperCase();
		}

		const targetContainer = document.getElementById("dynamic-recipe-container");
		document.getElementById("dynamicResultTitle").innerHTML = `RESULTADOS PARA "${resultPlaceholderText}"`;
		document.getElementById("dynamicResultDescription").innerHTML = ``;
		targetContainer.innerHTML = '';

		document.getElementById("homePageRandomRecipes").style.display = "none";

		populateRecipesFromAPI(targetQuery, targetContainer);
		return true;
	}

	return false;
}

window.populateRecipesFromURLParams = populateRecipesFromURLParams
window.populateRecipesFromAPI = populateRecipesFromAPI
