const path = require('path');
const express = require('express');


const app = express();

app.use('',express.static(path.join(__dirname, 'public')));

app.get('/', (request, response) => {
	return response.sendFile('login.html', { root: '.' });
});

app.get('/auth/discord', (request, response) => {
	return response.sendFile('index.html', { root: '.' });
});

const port = '5500';
app.listen(port, () => console.log(`App listening at http://localhost:${port}`));