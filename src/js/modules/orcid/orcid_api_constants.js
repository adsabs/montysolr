define(
  [],
  function(){
    var OrcidApiConstants = {
      Events :{
        LoginRequested: 'Orcid-Login-Requested',
        LoginSuccess: 'Orcid-Login-Success',
        LoginCancelled: 'Orcid-Login-Cancelled',
        SignOut: 'Orcid-Sign-Out',
        OrcidAction: 'Orcid-Action',
        UserProfileRefreshed: 'Orcid-User-Profile-Refreshed',
        IsBulkInsertMode: 'Orcid-is-Bulk-Insert-Mode'
      }
    };

    return OrcidApiConstants;
  }
);