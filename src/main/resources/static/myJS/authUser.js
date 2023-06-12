function isSessionTokenExpired() {
  const sessionToken = document.cookie
    .split('; ')
    .find(row => row.startsWith('session-token='))
    .split('=')[1];
    
  if (!sessionToken) {
    return true;
  }

  const expiration = new Date(parseInt(sessionToken));
  const currentMoment = new Date();

  return expiration < currentMoment;

}


/*
window.isSessionTokenExpired = isSessionTokenExpired;

window.addEventListener("load", (e) => {
	if ( isSessionTokenExpired() || sessionStorage.getItem("user-info") === null ) {

		fetch('/api/perfil')
			.then(response => response.json())
			.then(data => {
				sessionStorage.setItem('user-info', JSON.stringify(data));
			})
			.catch(error => {
				console.error('Error retrieving user profile:', error);
			});
	} else {

	}

});
*/
