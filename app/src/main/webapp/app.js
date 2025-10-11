const button = document.getElementById("helloButton");
const result = document.getElementById("result");

async function fetchGreeting() {
    result.textContent = "Fetching greeting...";
    result.classList.remove("error");

    try {
        const response = await fetch("hello", {
            headers: {
                Accept: "text/plain",
            },
        });

        if (!response.ok) {
            throw new Error(`Request failed with status ${response.status}`);
        }

        const text = await response.text();
        result.textContent = text;
    } catch (error) {
        console.error(error);
        result.textContent = "Could not load greeting. Please try again.";
        result.classList.add("error");
    }
}

button.addEventListener("click", () => {
    fetchGreeting();
});
