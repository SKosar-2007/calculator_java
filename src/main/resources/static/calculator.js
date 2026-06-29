const API = '/api';
let activeOpBtn = null;

async function post(endpoint, body = {}) {
    const res = await fetch(`${API}/${endpoint}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    });
    return res.json();
}

function updateDisplay(data) {
    const display = document.getElementById('display');
    const indicator = document.getElementById('memoryIndicator');
    display.textContent = data.display;
    indicator.textContent = data.hasMemory ? 'M' : '';

    if (data.display === 'Cannot divide by zero' || data.display === 'Error') {
        display.classList.add('error');
    } else {
        display.classList.remove('error');
    }
}

function clearHighlight() {
    if (activeOpBtn) {
        activeOpBtn.style.background = '';
        activeOpBtn = null;
    }
}

function highlightOp(symbol) {
    clearHighlight();
    const buttons = document.querySelectorAll('.btn.op');
    for (const btn of buttons) {
        if (btn.textContent === symbol) {
            btn.style.background = '#cc7a00';
            activeOpBtn = btn;
            break;
        }
    }
}

const opSymbols = { '+': '+', '-': '−', '*': '×', '/': '÷' };

async function digit(d) {
    clearHighlight();
    const data = await post('digit', { digit: d });
    updateDisplay(data);
}

async function decimal() {
    clearHighlight();
    const data = await post('decimal');
    updateDisplay(data);
}

async function operator(op) {
    const data = await post('operator', { operator: op });
    updateDisplay(data);
    highlightOp(opSymbols[op] || op);
}

async function equals() {
    clearHighlight();
    const data = await post('equals');
    updateDisplay(data);
}

async function clearAll() {
    clearHighlight();
    const data = await post('clear');
    updateDisplay(data);
}

async function negate() {
    clearHighlight();
    const data = await post('negate');
    updateDisplay(data);
}

async function percent() {
    clearHighlight();
    const data = await post('percent');
    updateDisplay(data);
}

async function memClear() {
    const data = await post('memory/clear');
    updateDisplay(data);
}

async function memRecall() {
    clearHighlight();
    const data = await post('memory/recall');
    updateDisplay(data);
}

async function memAdd() {
    const data = await post('memory/add');
    updateDisplay(data);
}

async function memSubtract() {
    const data = await post('memory/subtract');
    updateDisplay(data);
}

async function memStore() {
    const data = await post('memory/store');
    updateDisplay(data);
}

// Keyboard support
document.addEventListener('keydown', (e) => {
    if (e.key >= '0' && e.key <= '9') {
        digit(parseInt(e.key));
    } else if (e.key === '.') {
        decimal();
    } else if (e.key === '+') {
        operator('+');
    } else if (e.key === '-') {
        operator('-');
    } else if (e.key === '*') {
        operator('*');
    } else if (e.key === '/') {
        e.preventDefault();
        operator('/');
    } else if (e.key === 'Enter' || e.key === '=') {
        equals();
    } else if (e.key === 'Backspace') {
        backspace();
    } else if (e.key === 'Escape') {
        clearAll();
    } else if (e.key === '%') {
        percent();
    }
});

async function backspace() {
    clearHighlight();
    const data = await post('backspace');
    updateDisplay(data);
}
