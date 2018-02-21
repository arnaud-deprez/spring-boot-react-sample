
export default class GreetingService {
    async greetings(name) {
        let response = await fetch(`/api/greetings?name=${name}`, {
            accept: 'application/json'
        });

        if (response.ok)
            return response.json();
        else
            return response.text().then(text =>
                ({
                    id: -1,
                    content: `Error: ${response.statusText} -> ${text}`
                }));
    }
}