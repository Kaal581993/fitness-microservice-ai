import { Box, Button, styled } from "@mui/material"
import { useEffect, useState } from "react"
import { useAuthContext } from "react-oauth2-code-pkce"
import { useDispatch } from "react-redux";
import { BrowserRouter as Router, Navigate, Route, Routes, useLocation } from "react-router"
import { logout, setCredentials } from "./store/authSlice";
// import ActivityForm from "./components/ActivityForm";
// import ActivityList from "./components/ActivityList";
// import ActivityDetail from "./components/ActivityDetail";

import './App.css'
import ActivityForm from "./components/ActivityForm";
import ActivityDetails from "./components/ActivityDetails";
import ActivityList from "./components/ActivityList";

// ============================================
// Method 1: Using sx prop (Quick inline styles)
// ============================================
// The sx prop allows you to pass custom styles that
// will override Material UI's internal styles

// ============================================
// Method 2: Using styled() API (Reusable components)
// ============================================


const StyledButton = styled(Button)(({ theme }) => ({
  borderRadius: 20,
  textTransform: 'none',
  backgroundColor: 'rgba(19, 44, 70, 0.82)!important',
  color: '#ffffffff',
  margin:'5px',
  fontWeight: 600,
  padding: '10px 24px',
  boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
  transition: 'all 0.3s ease',
  '&:hover': {
    transform: 'translateY(-2px)',
    boxShadow: '0 6px 12px rgba(0,0,0,0.15)',
    backgroundColor: 'rgba(19, 44, 70, 0.82)',
    color: '#00b7ffff',
  },
}));

// Styled button with custom variant
const PrimaryButton = styled(Button)(({ theme }) => ({
  background: 'linear-gradient(45deg, #FE6B8B 30%, #FF8E53 90%)',
  borderRadius: 25,
  border: 0,
  margin:'5px',
  color: 'white',
  height: 48,
  padding: '0 30px',
  boxShadow: '0 3px 5px 2px rgba(255, 105, 135, .3)',
  '&:hover': {
    background: 'linear-gradient(45deg, #FF8E53 30%, #FE6B8B 90%)',
  },
}));

// ============================================
// Method 3: Using classes prop (Override MUI internals)
// ============================================
// When you need to override specific MUI internal classes
// even when using multiple classNames


const ActivitiesPage = () => {
  return (
    <div>
      <h2>Activities</h2>
      <Box component="section" 
      sx={{ p: 2, border: '1px dashed grey' }}>

        <ActivityForm onActivitiesAdded ={
          () => window.location.reload()
        }/>
        <ActivityList />

      </Box>
      </div>
  );

}

function App() {

  const { token, tokenData, logIn, logOut } = useAuthContext()

  const dispatch = useDispatch();

  const [authReady, setAuthReady] = useState(false);
  
  useEffect(() => {
    if (token) {
      dispatch(setCredentials({ token, user: tokenData }));
      setAuthReady(true);
    }
  }, [token, tokenData, dispatch]);

  return (
    <>
    <div>
      <h1>Welcome to Fitness Tracker</h1>
      <Router>
        {!token ? (
        /* Method 1: sx prop - fastest way to add custom styles */
        <Button 
          variant="text" 
          sx={{ 
            backgroundColor: 'rgba(19, 44, 70, 0.82)',
            color: '#ffffffff',
            margin:'5px',
            padding:'10px 20px',
            fontWeight: 'bold',
            '&:hover': {
              backgroundColor: 'rgba(19, 44, 70, 0.82)',
                          color: '#00b7ffff',

            }
          }}

          onClick={() => {logIn()}}
        >
          Login
        </Button> ) :(

          <div>
          <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
              <Button 
                variant="contained" 
                sx={{ 
                  backgroundColor: 'rgba(19, 44, 70, 0.82)',
                  color: '#ffffffff',
                  margin:'5px',
                  padding:'10px 20px',
                  fontWeight: 'bold',
                  '&:hover': {
                    backgroundColor: 'rgba(19, 44, 70, 0.82)',
                                color: '#00b7ffff',

                  }
                }}

                onClick={() => {logOut()}}
              >
                Logout
              </Button>
                  <Routes>
                    <Route path="/activities" element={<ActivitiesPage />}/>
                    <Route path="/activities/:id" element={<ActivityDetails />}/>
                    <Route path="/" 
                    element={token ? 
                    <Navigate to="/activities" replace /> : <div>
                      <h1>Welcome! Please Login!</h1>
                    </div>
                  }/>
                  </Routes>

              </Box>
          </div>
        )}
        
        {/* Method 2: Using styled() API - for reusable styled buttons */}
        <StyledButton variant="contained">
          Get Started
        </StyledButton>
        
        {/* Method 3: Custom className with sx override */}
        <Button 
          className="custom-button my-class another-class"
          variant="outlined"
          sx={{
            // Use & to target the internal root element
            '&.custom-button': {
              borderColor: '#ff4081',
              margin:'20px',
              color: '#ff4081',
              '&:hover': {
                backgroundColor: 'rgba(255, 64, 129, 0.1)',
                borderColor: '#f50057',
              }
            }
          }}
        >
          Learn More
        </Button>
        
        {/* Method 4: Using styled component with props
        <PrimaryButton>
          Get Started
        </PrimaryButton>
        
        {/* Method 5: Using classes prop to override MUI internals 
        <Button
          variant="contained"
          classes={{
            root: 'button-root',
            contained: 'button-contained',
            containedPrimary: 'button-contained-primary',
          }}
          className="additional-class"
          sx={{
            // This sx will merge with classes overrides
            '&.button-contained-primary': {
              backgroundColor: '#9c27b0',
              '&:hover': {
                backgroundColor: '#7b1fa2',
              }
            }
          }}
        >
          Upgrade
        </Button> */}
        
      </Router>
      
    </div>

    </>
  )
}

export default App
