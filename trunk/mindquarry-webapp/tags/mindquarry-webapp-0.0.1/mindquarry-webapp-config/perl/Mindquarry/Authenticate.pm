package Mindquarry::Authenticate;

use strict;
use warnings;

use LWP::UserAgent;
use Params::Validate qw[];
use Apache2::Const -compile => qw(OK DECLINED HTTP_UNAUTHORIZED);
use Apache2::RequestRec;
use Apache2::ServerUtil ();
use Apache2::Access ();
use Apache2::RequestUtil ();
use Apache2::Log ();

sub handler {
	my $r = shift;
	my $base = $r->dir_config('AuthBase');
	my $s = Apache2::ServerUtil->server;
	# Let subrequests pass.
	#return Apache2::Const::DECLINED unless $r->is_initial_req;
	
	# Get the client-supplied credentials.
	my ($status, $password) = $r->get_basic_auth_pw;
	#return $status unless $status == Apache2::Const::OK;
	# Perform some custom user/password validation.
	return Apache2::Const::OK if authenticate($r->user, $password, $base . $r->path_info);
	# Whoops, bad credentials.
	$r->note_basic_auth_failure;
	return Apache2::Const::HTTP_UNAUTHORIZED;
}

sub authenticate {
	my ($username, $password, $url) = @_;
	my $agent = LWP::UserAgent->new;
	my $override = sprintf '%s::get_basic_credentials', ref $agent;
	my $response;

	my $s = Apache2::ServerUtil->server;
	$s->log_error("AuthURL: " . $url . " User: " . $username);
	
	no strict   'refs';
        no warnings 'redefine';
	
	local *$override = sub {
            return ( $username, $password );
        };
	
	$response = $agent->head($url);
	
	
	if ($response->code == 401) {
		return 0;
	}
	if ($response->is_error) {
		$s->log_error("Error Message: " . $response->status_line);
		return 0;
	}
	return 1;
}

1;
