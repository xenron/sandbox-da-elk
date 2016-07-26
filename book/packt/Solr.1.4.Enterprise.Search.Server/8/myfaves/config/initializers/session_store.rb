# Be sure to restart your server when you modify this file.

# Your secret key for verifying cookie session data integrity.
# If you change this key, all old sessions will become invalid!
# Make sure the secret is at least 30 characters and all random, 
# no regular words or you'll be exposed to dictionary attacks.
ActionController::Base.session = {
  :key         => '_myfaves_session',
  :secret      => '2161b747444fd72432393579173470c71341c8b0d8841db1a70aeb184e9950b01ccb1d79489681e2a11a2ea6f2ed5dc37ae78441004e2cd783ac0e37b33f794f'
}

# Use the database for sessions instead of the cookie-based default,
# which shouldn't be used to store highly confidential information
# (create the session table with "rake db:sessions:create")
# ActionController::Base.session_store = :active_record_store
