(() => {
  const form = document.querySelector('[data-password-form]');
  if (!form) return;
  const message = document.querySelector('[data-message]');
  const token = new URLSearchParams(location.search).get('token');
  const reset = Boolean(token);
  form.addEventListener('submit', async event => {
    event.preventDefault();
    const data = Object.fromEntries(new FormData(form));
    if (reset && data.newPassword !== data.confirmPassword) {
      message.textContent = 'Passwords do not match.';
      return;
    }
    try {
      const response = await fetch(reset ? '/api/auth/reset-password' : '/api/auth/forgot-password', {
        method: 'POST', credentials: 'same-origin',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(reset ? {token, newPassword: data.newPassword, confirmPassword: data.confirmPassword} : data)
      });
      const result = await response.json().catch(() => ({}));
      if (!response.ok) throw new Error(result.error || 'Something went wrong.');
      form.innerHTML = `<p class="success">${result.message}</p><p><a href="sign-in.html">${reset ? 'Go to sign in' : 'Back to sign in'}</a></p>`;
    } catch (error) {
      message.textContent = error.message;
    }
  });
})();
