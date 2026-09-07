// Profile avatar upload helper: waits for profile avatar element, replaces with img and enables upload
(function(){
  let tries=0;
  const maxTries=40;
  const t=setInterval(async ()=>{
    tries++;
    const wrap=document.querySelector('.profile-hero .profile-avatar');
    if(!wrap){ if(tries>maxTries) clearInterval(t); return; }
    clearInterval(t);
    try{
      const img=document.createElement('img'); img.id='profile-avatar-img'; img.style.width='64px'; img.style.height='64px'; img.style.borderRadius='50%'; img.style.objectFit='cover';
      // obtain current user from window.Kindr if available, otherwise fall back to calling getMe
      const getUser = async () => {
        if(window.Kindr && typeof window.Kindr.currentUser === 'function'){
          let u = window.Kindr.currentUser();
          if(u) return u;
          if(typeof window.Kindr.getMe === 'function'){
            await window.Kindr.getMe();
            return window.Kindr.currentUser();
          }
        }
        return null;
      };
      const user = await getUser();
      img.src = (user && user.avatarUrl) ? user.avatarUrl : '';
      img.alt = 'Avatar';
      const initials=document.createElement('div'); initials.id='profile-avatar-initials'; initials.style.display = img.src? 'none':'flex'; initials.style.alignItems='center'; initials.style.justifyContent='center'; initials.style.width='64px'; initials.style.height='64px'; initials.style.borderRadius='50%'; initials.style.background='#0f766e'; initials.style.color='#fff'; initials.style.fontWeight='700'; initials.style.fontSize='20px'; initials.textContent = user? ( (user.firstName||'')[0] + (user.lastName||'')[0] ):'';
      img.onerror = function(){ this.style.display='none'; initials.style.display='flex'; };
      const input=document.createElement('input'); input.type='file'; input.accept='image/*'; input.style.display='none'; input.id='profile-avatar-input';
      wrap.innerHTML=''; wrap.appendChild(img); wrap.appendChild(initials); wrap.appendChild(input);
      wrap.style.cursor='pointer';
      wrap.addEventListener('click', ()=> input.click());
      input.addEventListener('change', async ()=>{
        if(!input.files || !input.files.length) return;
        const file=input.files[0];
        const fd=new FormData(); fd.append('image', file);
        try{
          const res=await fetch('/api/users/me/avatar',{method:'PATCH',credentials:'same-origin',body:fd});
          if(!res.ok){ console.error('Avatar upload failed',res.status); window.Kindr.toast('Could not upload avatar'); return; }
          if(typeof window.Kindr.getMe === 'function') await window.Kindr.getMe();
          const newUser = window.Kindr && typeof window.Kindr.currentUser === 'function' ? window.Kindr.currentUser() : null;
          if(newUser && newUser.avatarUrl){ img.src = newUser.avatarUrl; img.style.display='block'; initials.style.display='none'; }
          if(window.Kindr && typeof window.Kindr.toast === 'function') window.Kindr.toast('Profile image updated');
        }catch(err){console.error(err); window.Kindr.toast('Upload failed');}
      });
    }catch(e){console.error('avatar helper',e);} },250);
})();
