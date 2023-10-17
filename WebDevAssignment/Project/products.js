const api = "http://127.0.0.1:5000";

window.onload = () => {
    // BEGIN CODE HERE

    // Ορίζουμε const variables που καλούν τις συναρτήσεις όταν πατηθεί
    // το αντίστοιχο κουμπί
    const save_addition = document.getElementById("save_button");
    save_addition.onclick = productFormOnSubmit;
    const search = document.getElementById("search_button");
    search.onclick = searchButtonOnClick;
    // END CODE HERE
}

searchButtonOnClick = () => {
    // BEGIN CODE HERE
    // Αντλούμε πληροφορίες απο το products.html αρχείο, όπως το input-parameter για το search,
    // το id του πίνακα ώστε να κάνουμε append τα αποτελέσματα και το id του button
    const resultsTableBody = document.querySelector('#results-table tbody');
    const searchInput = document.getElementById('search-input').value; // Assuming you have an input field with the ID 'search-input'
    searchButton = document.getElementById('search_button')
    
    // Αυτή η γραμμή κώδικα αποτρέπει το να κάνει στιγμιαία εμφάνιση των αποτελεσμάτων και μετά να τα εξαφανίσει
    searchButton.disabled = true;
    
    fetch(api + '/search?name=' + encodeURIComponent(searchInput))
    .then(response => response.json())
    .then(results => {
      resultsTableBody.innerHTML = '';
      results.forEach(result => {
        const row = document.createElement('tr');
        row.innerHTML = `
          <td>${result._id}</td>
          <td>${result.name}</td>
          <td>${result.production_year}</td>
          <td>${result.price}</td>
          <td>${result.color}</td>
          <td>${result.size}</td>
        `;
        resultsTableBody.appendChild(row);
      });

      searchButton.disabled = false;
    })
    .catch(error => {
      console.error('Error:', error);
      searchButton.disabled = false;
    });
    // END CODE HERE
}

productFormOnSubmit = (event) => {
    // BEGIN CODE HERE
    // Βασικά βήματα υλοποίησης POST request στο αντίστοιχο endpoint του app.py
    const res = new XMLHttpRequest();
    res.open("POST", api+'/add-product');
    res.onreadystatechange = () => {
        if (res.readyState == 4) {
            if (res.status == 200) {
                alert(res.responseText);
            }
        }
    };

    // μεταβλητές που αντιστοιχούν στα text-fields του form
    // από τις οποίες αντλούμαι τις τιμές για να προσθέσουμε νέο προιόν στη βάση. 
    const name = document.getElementById("name");
    const production_year = document.getElementById("prod_year");
    const price = document.getElementById("price");
    const color = document.getElementById("color");
    const size = document.getElementById("size");

    res.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    // Στέλνουμε τις επιθυμητές τιμές στο endpoint του flask rest api 
    res.send(JSON.stringify({
        "name": name.value,
        "production_year": production_year.value,
        "price": price.value,
        "color": color.value,
        "size": size.value
    }));
    // Καθαρίζουμε τα forms
    name.value = "";
    production_year.value = "";
    price.value = "";
    color.value = "";
    size.value = "";
    
    // Το χρησιμοποιούμε για να μην κάνει "reload" τη σελίδα. Αν λείπει αυτό μας στέλνει στην αρχή της σελίδας
    event.preventDefault();
    // END CODE HERE
}
