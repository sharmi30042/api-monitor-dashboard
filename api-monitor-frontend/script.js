let performanceChart;

// 1. Theme Switcher Logic
function applyTheme() {
    const theme = document.getElementById('themeSelect').value;
    document.documentElement.setAttribute('data-theme', theme);
    
    // Smoothly update chart colors based on theme
    const accentColor = getComputedStyle(document.documentElement).getPropertyValue('--accent').trim();
    if (performanceChart) {
        performanceChart.data.datasets[0].borderColor = accentColor;
        performanceChart.data.datasets[0].backgroundColor = accentColor + "33"; // Add transparency
        performanceChart.update();
    }
}

// 2. Fetch History and Build UI
async function loadHistory() {
    try {
        const response = await fetch('http://localhost:8080/api/history');
        const data = await response.json();
        
        const historyBody = document.getElementById('historyBody');
        historyBody.innerHTML = ''; 

        data.forEach(log => {
            const row = `<tr>
                <td>${log.url}</td>
                <td><span class="status-${log.statusCode}">${log.statusCode}</span></td>
                <td>${log.responseTimeMs}ms</td>
                <td>${log.isSlow ? '⚠️ YES' : '✅ NO'}</td>
            </tr>`;
            historyBody.innerHTML += row;
        });

        updateChart(data);
    } catch (error) {
        console.error("Error loading history:", error);
    }
}

// 3. Run New Test
async function runTest() {
    const url = document.getElementById('apiUrl').value;
    const method = document.getElementById('apiMethod').value;
    const btn = document.getElementById('testBtn');

    if(!url) return alert("Enter a URL!");

    btn.innerText = "Testing...";
    btn.disabled = true;

    try {
        await fetch(`http://localhost:8080/api/test?url=${url}&method=${method}`);
        loadHistory(); // Refresh view
    } catch (error) {
        alert("Server error!");
    } finally {
        btn.innerText = "Run Test";
        btn.disabled = false;
    }
}

// 4. Performance Chart Logic
function updateChart(data) {
    const labels = data.map(log => new Date(log.createdAt).toLocaleTimeString());
    const times = data.map(log => log.responseTimeMs);
    const accent = getComputedStyle(document.documentElement).getPropertyValue('--accent').trim();

    const ctx = document.getElementById('performanceChart').getContext('2d');
    
    if (performanceChart) performanceChart.destroy();

    performanceChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Response Time (ms)',
                data: times,
                borderColor: accent,
                backgroundColor: accent + "22",
                fill: true,
                tension: 0.4
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { display: false } },
            scales: { y: { beginAtZero: true } }
        }
    });
}

// Initialize on Load
window.onload = loadHistory;