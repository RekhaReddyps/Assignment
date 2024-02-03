import React from 'react'
import mavericHeader from '../assets/maveric-logo-updated.png'

function Header() {

  const logout = (e) => {
    window.location.href = '/';    
    localStorage.removeItem("token");
    localStorage.removeItem("user"); 
  }

  var classname = (localStorage.getItem("token")) ? "header-logout-button" : "display";

  return (
    <div className='header'>
        <span><img className='header-img' src={mavericHeader} alt="Maveric" /> </span>
        <span className='header-text'>Maveric Bank App</span>

        <div className='header-logout'>
          <button className={classname} onClick={logout} type='submit'>Logout</button>
        </div>
    </div>
  )
}

export default Header